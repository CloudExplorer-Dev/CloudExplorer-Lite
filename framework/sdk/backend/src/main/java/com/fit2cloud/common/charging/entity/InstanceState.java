package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/22  14:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class InstanceState {

    /**
     * 实例状态
     */
    private final List<State> stateList;
    /**
     * 实例进制
     */
    private final int radix = 10;

    private int mark;

    public List<State> getStateList() {
        return stateList;
    }

    public static void main(String[] args) {
        InstanceState a = new InstanceState("62vewxplhiiavq8qawl9ad88lgnuo1j5wq1sak7v5x8x7z9ou6s59hnuv3478nmu50aj39turkg8a31mw0nb98j6w81fo5ysi2azdy4r9qo9iyau2lx1xihidzmcdjjdr7etww4r90u9kfkfilqajpynhxiz6vehwy2pgviswnvgcdz55eq9ka0m34lp39vzz1qrdjqt5wm4j9ih6jodp30yzmrb057m598t66izbhe206to1ban0ea24hnu5jcd14mtu5ci587qn79ntk63tqxewpqnksnlecmy1mrgejfv4n5n44z9mjlatkovi2r5dws7kuh2mn0tuy0ij1qqllzeev0l1lmdsml0zm0i5urfu1vl6jtqid7n3nwudd74vy3ubccgnbvbmb1iqadoxyoof4tzmklk8nqvuu6x46s06f8fnd9tqbep9ofyfzc45tnte3ph37iz4e3tk9ozmzs7vj8dlwnz2rptfd078qzxsh4vm6w7fzr5zvnz1bpnw7eqmkwbiju8we9d6rrapaykb0z0iqcdouqdyp64tep58ozohto0jalcvwo90x2nw1et9m7s5sbigwf2f4wdfjdx6zy6lw2kbjnkl686kfs1o3isdrfnyuw0cr5lh0s8t3vf7ay15rvj1cpfpsqth70oclrcv2sk3o5pbu61wo6uky8c72p3rmh9tx9wzso0vmlsa5r5sb15hqo1jdqq0s8qzhbzueq174yt2vj34snxbhto5vudztw9knkfma3u0gsfs6e4dsth85pcust7u32vrzkm20nusz2j4atdwvieonplims7xv1csgcx0ne3hissjd0fhcd8htcw5az15x356w22clufdl4xi9atoxo0c3wsatr485xctziq4yl7bkcgq11qlhrru1kmhpztwk6r1qjecjjyp1kpkv1gjkthjic7tntmohqv0hk8dgvkwx4du2detvakwh47dhoxpdbz1shoosa691xrtmdwizrkag27sqquvipdijl62xsm5i50uqqxoat592h08zo8sdfvspcg3o5eq5at4mocztpx4303xycv6z40bvv618kxbuy9seqjj8y7ul6a0dnaxu56t7em4n7m32k7agpswj9x9hszseov68ks2x22rbb572c9yle8nd6pgq9etjwka34qqtt3v8rmp26yd7bpac7d3h0ibh8yrl2j60b1o7ldyz7jpkj3v8gi3gzb3mf09mrabiiudecj6budamqi85vqutjbnxvdqatps5hfp6tko8g5d8gel84tjyq3tyuvp77w2g08qzmuj4jva0837kinnkxcyt1lozphqsi0fh31b51hm3we8dj9eioznp556gg90tq6e1fesfn6mklnz9aaoyj3eesp7eide77rk0ypogmnywbut39pm1ux51156arrvx0ueelhlrwwhk6t9bqb2s1dwezr394wo2kdmbtuat9adutzaq7a3op9xqasen5tkuu7hsr46ucxgg1uxujx9p44gkw411utz1xao93edxfjzqmskpbw7pwv6fjsl9yc1i3t9ubz32jdcmdv339y8hu1jdb1y1dj4cdi9ptesww5tu57h2kaljpry0s2dzxvv63egk1z4wogp45aklx2nhssabm6faippewz1mozz8czmyuy6rfoa6wfwa8ne6y3ordro87rc98xyzjgwf4vusv4ldza60ujllil6av0pktiih52guw4w7g6g3q6o0dpagcr8izy21knovqnhcfv8g93ol8v6kdu6qxkfbor2crxox802fwjl34iqf06t966xa9s2r42vpyszq7wrei7olcb1ov0jyi4752r6acwy9lxccpt7ctvq78259hwvk98zezj2wjic8gitu53r7oset323zpfmyxuvmgu20juojur96tvv1elzh4icuju8h2bds6rumfzexi8gi1739hmrrok8t1dotrrteh8c39aag8tuuwgjzmwqli9j9nhns8raifbdqi1iwnqqe9c87yhjsg7u01dog1izqbppozj778gfw49mqykd0nf96zcn024zj1sbk8duovqv9gj35v4qpfis3ekdr7ycbrgnk8sueycm95mqxdk1h3zh7228osm9o0e8tmti5c5avtjaqbvoycpfkmgkh963plgg38mflzqz53xjj8py6hoxjx7b666huh0kklqvccr5zpa3vqp3rpha2fvd35xt81ca5qlle6y1zklszzt2s8nv2hva50nu3r280k54uife0khisbzmlwe5z0lcjtyla5cs73foocjxe8hm8lohn5246xl3bq1g2emy84oh3sh1qi3ppjzqeup41zp6reu2g4upatai5r0ta34rhg47rqqllrwmf9kplqnd33l8inzfvju0vvz2qevrp4k20ug6zffs48py4sxp4dp221u51ma2vzqnbja7ldaae7p33fl5ilwhqm4xpjvp9bp58tqamgu6ylygqmdu1508mhqqp0ckp5x3jcpkvmjorg3ebk900dken6gon30i75ovpjileps8bb53wsgncbuj89fzomtrxayjev2tgan6l4btaddekxfvgrwssviwdi9mviu2mw74fdz74q4el53zw6eg68od7uv2drf7asj7bafh517kis4h1kn4x29ibyufobozcgiuwz23m9uuacnfjk2r1iqimj1oqzatoor242jiehmdamemgjup00s3sm8fmbtp6vls5phlop2riv9icmu1t8noxvd9xgjediyyi6i78nr435gret1snk81nzqzg92wyccusq12wjninr80izewjp2anunso8wxqdlbv0uk7nv0dojszxk88uwxy7to7a2r1i9swclw80b6crbjcxalm62hdeifbhmb55rsci0rl8905qjk4qlml0gf0du9bi4o3x63g6dwvz8zhavdbfaltwzpvx5zz40445nhlgt41nnpmmnda6jla3g2avs1s82hfoawo9jajcbu2be1zze7x4rwig7z5r7fie81nfgrkvnjvmr8w65dvlwndexsanonezw1bqbh6p869j2z2imotngkdth8tv1pod0f8khymiiosdfk99rqa0ysw47v844fp02mh8vwb3i2rw2eszvaxghjpbq0d6e9pra93cd0yzol7xx6vm0sik2s9utva3lzdvjowkvxzrn0822owol2b8uwy8ntjtrg9vwk6i2vd1dugz3amjvt9yxjspqkaou2lzu6g8z4cta3p5pey1xivz1u4e4x1xp2ryyap28qf16rgttmlfwy69hd51ohpyag398eo6k8si283u0ehnzmyi2w6o6os51ga580k4kdh2xi08aczq2oj1z1972efngirz7ihxk39nmyx4h3f04g98ychawcfnq3ffye6b4gnh4ia49o3nqpgnlpc4fa4c4014qyt82vi5evvgdwar9lrcco2jv9pabvouerz9gefbayti5o0g0m5xqq9kco3qhy5fwuu1w26wi9l1zln24ehk1yljqw6wcaok21uyleci9wunrvh7wc37tcr8huliktohtifn0o0o6xnu35tvx353mvu9jsczm4i793289q4j4ogtzgjkfbjvoovgm9lmd7khhnvpya2eyh48xu54odsj3lyq3dnhswodvwvb3qy673rsesbdmc2x3g1dvxefb43898ej43nft09ircy89qlo21ibwwe5h4sgoa7fjmjhbzx1gijdxcspaysnxfz3a4hp76bqneqvr6a6e1d842fxpmxhayhkyw2p7whpuzjkpt5wc2xzdt5n85z0ofngbbaefii3b3q8adgpch4vmkmhg82gk0clefdaxo8t01ve1q31suf84739zwc8ocpinxvhk41r6wce64y2crthyvob87ywe5fkubu056k7xuynyqgmw68j6b4fpzpe3lexqsnig4x795hrvl8ff7drt0vzelnccdm32iegqwf3fr7o901k0oepd41qmcspm446jingl5ylpq6koynoajm6itqnw7osncy8i9xk1pfa5fhzlm88bnmmlhy8cxhyjpxrqwsua1n43h3foxnvq8inq4m8qt363xgzty2eg0x089vwmc5kfs1f6tc93r89b2hdoqyffyxodwhsmhz3rqj22uc7sewz8y821n1usy5bgng2f5g0m24s8o3xijdzdi648h7eej6y82udvlihifjpopkckiuiryq90w5g7nbnbm5hvyy7wtr5dvu4fbrro7kqwsf6mge6l9th1cbcr8sg9ts31qfqmww8fj6mwpfnigllplm2fx2ptkm9j3sqkyrk4ipuf9srcjatdknfb62a0ttpdirl14hvh19u4kyaxfzhi13ils45yrdc96l9u79yj7lba3mgqp7g70agwzc3h4nziue48aqh8cl7xcuqfdb0nyju52wtftrlpmzayb16oun4mn6igsevx9q1v9l3i6hwixp0y4k3158biy9yydc9dfs69zxw5m5tq717ai59yyj3ueo3fl9qy9q92ezxk141pizlvw5dwly96v8tybyyez9ns0wpb5tahoj94sk254kd38af5e3l4trhihdib2o2aj6nikjyhl1rdsub6ac6ypkfy0e7ivowsbexhmnghwt68vluxcyx0tgqa1ep6xj6mq14h57o5pl1fuar8jopwutgtqwlevgknqj9p1z9m73x3xr5tguhogxo4kaycj6uafowm6ua3ftofv9q1fr9r8vd803uhrjnjoeqb5pe0sdw9hm8xyq0i1wc93pptsho0vj7vh3qjbmfia2piyj1ocrgz2he5iwhnqb9on6hf45zpoqw04q1lcolsq2b58qiu11l9eok61moer7jas1z4wshnnjhlfty9i90e45ficfuuwr1mu7a96hjoq3ldyx4935wfh6rronf2ndcajdnytbyqvnosod6gy9kel7r8tfju6mnr759khhsldw1defegronoguj0vq3qkane383ujdh2y04kf40sgi904fw8dsd7qyfnnx7c9f1c47vp72ytjuqf10aw7huglojwcz7pjfbvgk3keqrpmxrwdfeo1x2zs449gyz2ry9jihsq3ue7xigvls8n26airlfwpk1u22eclz9zd1fuj2a2gcpoyf828yy7yw2pwziw6j6cgn4j4za916ir2ccoskq9ltcprdb1ozq81zebadj45mfvetwobmsh3tg5gc6mysrkqsfc0883o5oz7vw8wngx5rj4sdj1aqhmin0d5g2epb3dq9rj3wffpk7ywm2wn70foiqsj2ncgl4icjqc1xa3bu4yvc3k4put9n14tjg2gvqnaspsgp913m70yasyabd37bv3mno6dn5upbiimzekwcm8exjuwbiuistbunry80opxttpdbkrblazv3qn2tjhmlig5kpm080pk078ltdaoxongveoy0rkqkr0kyh7g0axcuwvr5prjo4tund7juemsxxkb24zsqqeukt8fejljv9rffs8meltxmt9hzgrzqzcahrw2c4r7y4f3zg7kpstebynzpkmz2iuu4esyoi5b37erthdw7lhjp0toe3197p4j9d4vmn8zsz2ucx9xqe4o5qi8vxcpw5lh3vty48kfasqd60bz7b99cwg6gfrko7q87v9zv995xrz483rloy3547eu69jy8hsy9oaldkhx031du6pynzdxdxzqjwcdggzwg2onw1to6cnlxri3wc4flpi9hv68nnp4mjmqdx7lmrz68wepm2gput8q90d6zh7llvs69olvo1luuaxvu74gz6x25zqvw8qw8ylz5wp6cjknsquinjm1ujdv2n74ir0d8bt1z79r233g3pn633iawrvwzdw88dlwe72q1s5bv85jvk35ymdft7opncbab7flc2panl66lcbcd5g2dszsihunx5gx77uxwjmms0usc096e1l0e53vfgef23pki27mci5tck5vemqhilxj9ukccu70jmall4i1vjk3h3ohv4hudthdk2absbmrhybiwfyvfrdqmug6n8dp2brj08bcltx18mqmz5v519wven4o08dmtska4pogglffe3q7w758x02mncqc50m7ks4fa26scvrf4mmglj0finntcfbuaqrdgyvvz15ckmi5u6ry1624872nh2ncjl5zi2h3z2rdcyiafr1rg4jx2v4vmm3jwvqiu1fxa6i3q6uqkg");
        int c = 0;
        for (int i = 0; i < a.stateList.size(); i++) {
            if (a.stateList.get(i).code() > '0') {
                System.out.println(i);
                c++;
            }
        }
        List<State> stateList1 = a.getStateList(null, Time.of(30, 10, 35));
        List<State> stateList2 = a.getStateList(Time.of(30, 10, 35), null);
        System.out.println(c);
    }

    /**
     * 实例状态空构造器
     */
    public InstanceState() {
        stateList = new ArrayList<>();
        this.mark = 0;
    }

    /**
     * 实例状态构造器
     *
     * @param state 状态数据
     */
    public InstanceState(String state) {
        String history = new BigInteger(state, 36).toString(this.radix);
        history = history.substring(1);
        this.mark = history.length();
        String reverseData = new StringBuilder(history).reverse().toString();
        this.stateList = toList(reverseData.toCharArray());
    }

    private List<State> toList(char[] chars) {
        List<State> instanceStateConstants = new ArrayList<>();
        for (char aChar : chars) {
            instanceStateConstants.add(new State(aChar));
        }
        return instanceStateConstants;
    }


    public String toDetailsString() {
        return this.stateList.stream().map(State::code).map(Object::toString).collect(Collectors.joining()) + '1';
    }

    @Override
    public String toString() {
        return new BigInteger(new StringBuilder(toDetailsString()).reverse().toString(), this.radix).toString(36);
    }

    /**
     * 修改
     *
     * @param start 开始下标
     * @param end   结束下标
     * @param state 状态
     */
    private void update(int start, int end, State state) {
        if (stateList.size() < start) {
            for (int i = stateList.size(); i < start; i++) {
                stateList.add(new State('0'));
            }
        }
        for (int i = start; i < (start == end ? end + 1 : end); i++) {
            if (i < this.stateList.size()) {
                stateList.set(i, state);
            } else {
                stateList.add(state);
            }

        }
        this.mark = stateList.size();
    }


    /**
     * 获取状态数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 状态
     */
    public List<State> getStateList(Time start, Time end) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        if (this.stateList.size() < endIndex) {
            endIndex = this.stateList.size() - 1;
        }
        if (this.stateList.size() < startIndex) {
            return List.of();
        }
        return this.stateList.subList(startIndex, endIndex);
    }

    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(String month,
                                                                        BillingGranularityConstants granularity) {
        return getStateListGroup(null, null, month, granularity);
    }

    /**
     * @param start       开始时间
     * @param end         结束时间
     * @param month       月份
     * @param granularity 根据月份分组
     * @return 粒度 -> 状态
     */
    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(Time start,
                                                                        Time end,
                                                                        String month,
                                                                        BillingGranularityConstants granularity) {
        if (granularity.equals(BillingGranularityConstants.MONTH)) {
            return List.of(new DefaultKeyValue<>(month, this.getStateList(start, end)));
        } else if (granularity.equals(BillingGranularityConstants.DAY)) {
            return groupDay(getStateList(start, end), start, month);
        } else if (granularity.equals(BillingGranularityConstants.HOUR)) {
            return groupHour(getStateList(start, end), start, month);
        } else {
            throw new RuntimeException("不支持的粒度");
        }
    }


    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据小时分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupHour(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(0, 0, 0);
        }
        String format = "yyyy-MM-dd HH";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day + " " + start.hour, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据天分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupDay(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(1, 0, 0);
        }
        String format = "yyyy-MM-dd";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 * 24 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60 * 24) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param state 状态
     */
    public void update(Time start, Time end, State state) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(startIndex, endIndex, state);
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param end   结束时间
     * @param state 状态
     */
    public void updateHistory(Time end, State state) {
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(this.mark, endIndex, state);
    }

    @Data
    public static class Time {
        /**
         * 日
         */
        private int day;
        /**
         * 小时
         */
        private int hour;
        /**
         * 分钟
         */
        private int minute;

        public static Time of(int day, int hour, int minute) {
            Time time = new Time();
            time.day = day;
            time.hour = hour;
            time.minute = minute;
            return time;
        }

        public static Time of(int hour, int minute) {
            Time time = new Time();
            time.day = 0;
            time.hour = hour;
            time.minute = minute;
            return time;
        }
    }


    static class Region {
        /**
         * 开始索引
         */
        private int start;
        /**
         * 结束索引
         */
        private int end;

        static Region ofDay(int day) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24;
            region.end = day * 60 * 24 + 60 * 24;
            return region;
        }

        static Region ofHour(int day, int hour) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60;
            region.end = day * 60 * 24 + hour * 60 + 60;
            return region;
        }

        static Region ofMinute(int day, int hour, int minute) {
            check(day, hour, minute);
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60 + minute;
            region.end = day * 60 * 24 + hour * 60 + minute;
            return region;
        }

        static void check(int day, int hour, int minute) {
            if (day < 1 || day > 31) {
                throw new RuntimeException("时间不对");
            }
            if (hour < 0 || hour > 23) {
                throw new RuntimeException("时间不对");
            }
            if (minute < 0 || minute > 59) {
                throw new RuntimeException("时间不对");
            }
        }
    }


    public record State(char code) {

        public State {
            if (code < 48 || code > 57) {
                throw new RuntimeException("实例状态只支持10种" + "'0'-'9'{48-57}");
            }
        }
    }
}
