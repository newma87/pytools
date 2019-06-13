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
        <createTable tableName="{{model.name}}">
            <column name="{{model.baseColumn}}" type="{{model.baseColumnType}}">
                <constraints nullable="false"/>
            </column>
            <column name="{{model.referenceColumn}}" type="{{model.referenceColumnType}}">
                <constraints nullable="false"/>
            </column>
        </createTable>
        <rollback>
            <dropTable tableName="{{model.name}}"/>
        </rollback>

        <addPrimaryKey columnNames="{{model.baseColumn}}, {{model.referenceColumn}}" tableName="{{model.name}}"/>

        <addForeignKeyConstraint baseColumnNames="{{model.baseColumn}}"
                                 baseTableName="{{model.name}}"
                                 constraintName="fk_{{model.name}}_{{model.baseColumn}}"
                                 referencedColumnNames="{{model.baseColumnRef}}"
                                 referencedTableName="{{model.baseTable}}"/>
        <addForeignKeyConstraint baseColumnNames="{{model.referenceColumn}}"
                                 baseTableName="{{model.name}}"
                                 constraintName="fk_{{model.name}}_{{model.referenceColumn}}"
                                 referencedColumnNames="{{model.referenceColumnRef}}"
                                 referencedTableName="{{model.referenceTable}}"/>
        {% else %}
        <addForeignKeyConstraint baseColumnNames="{{model.baseColumn}}"
                                 baseTableName="{{model.baseTable}}"
                                 constraintName="{{model.name}}"
                                 referencedColumnNames="{{model.referenceColumn}}"
                                 referencedTableName="{{model.referenceTable}}"/>
        {% endif %}
    {% endfor %}
    </changeSet>
</databaseChangeLog>