package com.fit2cloud.autoconfigure;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fit2cloud.dto.UserDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.io.Serial;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
public class RedisConfig {
    @Resource
    private ServerInfo serverInfo;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
                .disableCachingNullValues()
                .prefixCacheNameWith(serverInfo.getModule() + ":")
                .entryTtl(Duration.ofHours(1));
        //专门指定某些缓存空间的配置，如果过期时间【主要这里的key为缓存空间名称】
        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(map)
                .build();
    }

    @Bean("authKeyGenerator")
    public KeyGenerator authKeyGenerator() {
        return (target, method, params) -> {
            SecurityContext context = SecurityContextHolder.getContext();
            StringBuilder sb = new StringBuilder();
            if (context.getAuthentication().isAuthenticated()) {
                UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
                // 添加当前角色
                sb.append(userDto.getCurrentRole()).append(":");
                // 添加用户id
                sb.append(userDto.getUsername()).append(":");
            }
            // 添加类名
            sb.append(target.getClass().getSimpleName()).append(":");
            // 添加方法名
            sb.append(method.getName()).append(":");
            //调用SimpleKey的key生成器
            Object key = SimpleKeyGenerator.generateKey(params);
            return sb.append(key);
        };
    }

    @Bean("notAuthKeyGenerator")
    public KeyGenerator notAuthKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            // 添加类名
            sb.append(target.getClass().getSimpleName()).append(":");
            // 添加方法名
            sb.append(method.getName()).append(":");
            //调用SimpleKey的key生成器
            Object key = SimpleKeyGenerator.generateKey(params);
            return sb.append(key);
        };
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        CeGenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = serializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        //hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


    public CeGenericJackson2JsonRedisSerializer serializer() {
        return new CeGenericJackson2JsonRedisSerializer();
    }

    public static class CeGenericJackson2JsonRedisSerializer implements RedisSerializer<Object> {
        static final byte[] EMPTY_ARRAY = new byte[0];

        static boolean isEmpty(@Nullable byte[] data) {
            return data == null || data.length == 0;
        }

        private final ObjectMapper mapper;

        public CeGenericJackson2JsonRedisSerializer() {
            this((String) null);
        }

        public CeGenericJackson2JsonRedisSerializer(@Nullable String classPropertyTypeName) {
            this(new ObjectMapper());
            this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.mapper.registerModule(new JavaTimeModule());
            registerNullValueSerializer(this.mapper, classPropertyTypeName);
            StdTypeResolverBuilder typer = new CeGenericJackson2JsonRedisSerializer.TypeResolverBuilder(ObjectMapper.DefaultTyping.EVERYTHING, this.mapper.getPolymorphicTypeValidator());
            typer = typer.init(JsonTypeInfo.Id.CLASS, (TypeIdResolver) null);
            typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
            if (StringUtils.hasText(classPropertyTypeName)) {
                typer = typer.typeProperty(classPropertyTypeName);
            }

            this.mapper.setDefaultTyping(typer);
        }

        public CeGenericJackson2JsonRedisSerializer(ObjectMapper mapper) {
            Assert.notNull(mapper, "ObjectMapper must not be null!");
            this.mapper = mapper;
        }

        public static void registerNullValueSerializer(ObjectMapper objectMapper, @Nullable String classPropertyTypeName) {
            objectMapper.registerModule((new SimpleModule()).addSerializer(new CeGenericJackson2JsonRedisSerializer.NullValueSerializer(classPropertyTypeName)));
        }

        @Override
        public byte[] serialize(@Nullable Object source) throws SerializationException {
            if (source == null) {
                return EMPTY_ARRAY;
            } else {
                try {
                    return this.mapper.writeValueAsBytes(source);
                } catch (JsonProcessingException var3) {
                    throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
                }
            }
        }

        @Override
        public Object deserialize(@Nullable byte[] source) throws SerializationException {
            return this.deserialize(source, Object.class);
        }

        @Nullable
        public <T> T deserialize(@Nullable byte[] source, Class<T> type) throws SerializationException {
            Assert.notNull(type, "Deserialization type must not be null! Please provide Object.class to make use of Jackson2 default typing.");
            if (isEmpty(source)) {
                return null;
            } else {
                try {
                    return this.mapper.readValue(source, type);
                } catch (Exception var4) {
                    throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
                }
            }
        }

        private static class TypeResolverBuilder extends ObjectMapper.DefaultTypeResolverBuilder {
            public TypeResolverBuilder(ObjectMapper.DefaultTyping t, PolymorphicTypeValidator ptv) {
                super(t, ptv);
            }

            @Override
            public ObjectMapper.DefaultTypeResolverBuilder withDefaultImpl(Class<?> defaultImpl) {
                return this;
            }

            @Override
            public boolean useForType(JavaType t) {
                if (t.isJavaLangObject()) {
                    return true;
                } else {
                    t = this.resolveArrayOrWrapper(t);
                    if (ClassUtils.isPrimitiveOrWrapper(t.getRawClass())) {
                        return false;
                    } else {
                        return !TreeNode.class.isAssignableFrom(t.getRawClass());
                    }
                }
            }

            private JavaType resolveArrayOrWrapper(JavaType type) {
                while (type.isArrayType()) {
                    type = type.getContentType();
                    if (type.isReferenceType()) {
                        type = this.resolveArrayOrWrapper(type);
                    }
                }

                while (type.isReferenceType()) {
                    type = type.getReferencedType();
                    if (type.isArrayType()) {
                        type = this.resolveArrayOrWrapper(type);
                    }
                }

                return type;
            }
        }

        private static class NullValueSerializer extends StdSerializer<NullValue> {
            @Serial
            private static final long serialVersionUID = 1999052150548658808L;
            private final String classIdentifier;

            NullValueSerializer(@Nullable String classIdentifier) {
                super(NullValue.class);
                this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
            }

            @Override
            public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeStartObject();
                jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
                jgen.writeEndObject();
            }

            @Override
            public void serializeWithType(NullValue value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
                this.serialize(value, gen, serializers);
            }
        }
    }

}
