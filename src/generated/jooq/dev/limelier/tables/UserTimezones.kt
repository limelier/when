/*
 * This file is generated by jOOQ.
 */
package dev.limelier.tables


import dev.limelier.Public
import dev.limelier.keys.USER_TIMEZONES_PKEY
import dev.limelier.tables.records.UserTimezonesRecord

import java.util.function.Function

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row2
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UserTimezones(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, UserTimezonesRecord>?,
    aliased: Table<UserTimezonesRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<UserTimezonesRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.user_timezones</code>
         */
        val USER_TIMEZONES: UserTimezones = UserTimezones()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UserTimezonesRecord> = UserTimezonesRecord::class.java

    /**
     * The column <code>public.user_timezones.user_id</code>.
     */
    val USER_ID: TableField<UserTimezonesRecord, Long?> = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>public.user_timezones.timezone</code>.
     */
    val TIMEZONE: TableField<UserTimezonesRecord, String?> = createField(DSL.name("timezone"), SQLDataType.CLOB.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<UserTimezonesRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<UserTimezonesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.user_timezones</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.user_timezones</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.user_timezones</code> table reference
     */
    constructor(): this(DSL.name("user_timezones"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, UserTimezonesRecord>): this(Internal.createPathAlias(child, key), child, key, USER_TIMEZONES, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<UserTimezonesRecord> = USER_TIMEZONES_PKEY
    override fun `as`(alias: String): UserTimezones = UserTimezones(DSL.name(alias), this)
    override fun `as`(alias: Name): UserTimezones = UserTimezones(alias, this)
    override fun `as`(alias: Table<*>): UserTimezones = UserTimezones(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): UserTimezones = UserTimezones(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): UserTimezones = UserTimezones(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): UserTimezones = UserTimezones(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
