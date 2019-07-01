<?xml version="1.0" encoding="UTF-8"?>

<!-- Generated by AutoCoder 
    Author: {{author}}
    Create: 2018-05-12
 -->

<databaseChangeLog
xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">
    <changeSet id="initial-02" author="{{author}}" runOnChange="true">
    {% for model in models %}
        {% if model.relationship == "ManyToMany" %}
        <createTable tableName="{{model.tableName}}">
            <column name="{{model.baseColumn}}" type="{{model.baseColumnType}}">
                <constraints nullable="false"/>
            </column>
            <column name="{{model.slaveColumn}}" type="{{model.slaveColumnType}}">
                <constraints nullable="false"/>
            </column>
        </createTable>
        <rollback>
            <dropTable tableName="{{model.tableName}}"/>
        </rollback>

        <addPrimaryKey columnNames="{{model.baseColumn}}, {{model.slaveColumn}}" tableName="{{model.tableName}}"/>

        <addForeignKeyConstraint baseColumnNames="{{model.baseColumn}}"
                                 baseTableName="{{model.tableName}}"
                                 constraintName="{{model.keyName}}"
                                 referencedColumnNames="{{model.referenceColumn}}"
                                 referencedTableName="{{model.referenceTable}}"/>
        <addForeignKeyConstraint baseColumnNames="{{model.slaveColumn}}"
                                 baseTableName="{{model.tableName}}"
                                 constraintName="{{model.slaveKeyName}}"
                                 referencedColumnNames="{{model.slaveColumnRefColumn}}"
                                 referencedTableName="{{model.slaveColumnRefTable}}"/>
        {% else %}
        <addForeignKeyConstraint baseColumnNames="{{model.baseColumn}}"
                                 baseTableName="{{model.tableName}}"
                                 constraintName="{{model.keyName}}"
                                 referencedColumnNames="{{model.referenceColumn}}"
                                 referencedTableName="{{model.referenceTable}}"/>
        {% endif %}
    {% endfor %}
    </changeSet>
</databaseChangeLog>