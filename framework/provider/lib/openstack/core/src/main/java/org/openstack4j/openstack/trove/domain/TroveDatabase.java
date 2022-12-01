package org.openstack4j.openstack.trove.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.trove.Database;
import org.openstack4j.model.trove.builder.DatabaseBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Database
 *
 * @author sumit gandhi
 */

public class TroveDatabase implements Database {

    private String name;
    @JsonProperty("character_set")
    private String dbCharacterSet;
    @JsonProperty("collate")
    private String dbCollation;

    public static DatabaseBuilder builder() {
        return new DatabaseConcreteBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDbCharacterSet() {
        return dbCharacterSet;
    }

    public void setDbCharacterSet(String dbCharacterSet) {
        this.dbCharacterSet = dbCharacterSet;
    }

    @Override
    public String getDbCollation() {
        return dbCollation;
    }

    public void setDbCollation(String dbCollation) {
        this.dbCollation = dbCollation;
    }

    @Override
    public DatabaseBuilder toBuilder() {
        return new DatabaseConcreteBuilder();
    }

    public static class Databases extends ListResult<TroveDatabase> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("databases")
        private List<TroveDatabase> troveDatabaseList;

        public List<TroveDatabase> getTroveDatabaseList() {
            return troveDatabaseList;
        }

        public void setTroveDatabaseList(List<TroveDatabase> troveDatabaseList) {
            this.troveDatabaseList = troveDatabaseList;
        }

        @Override
        protected List<TroveDatabase> value() {
            return troveDatabaseList;
        }

    }

    public static class DatabaseConcreteBuilder implements DatabaseBuilder {

        private TroveDatabase database;

        public DatabaseConcreteBuilder(TroveDatabase troveDatabase) {
            this.database = troveDatabase;
        }

        public DatabaseConcreteBuilder() {
            this(new TroveDatabase());
        }

        @Override
        public Database build() {
            return database;
        }

        @Override
        public DatabaseBuilder from(Database in) {
            this.database = (TroveDatabase) in;
            return this;
        }

        @Override
        public DatabaseBuilder name(String name) {
            this.database.name = name;
            return this;
        }

        @Override
        public DatabaseBuilder dbCharacterSet(String dbCharacterSet) {
            this.database.dbCharacterSet = dbCharacterSet;
            return this;
        }

        @Override
        public DatabaseBuilder dbCollation(String dbCollation) {
            this.database.dbCollation = dbCollation;
            return this;
        }

    }

}
