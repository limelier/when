import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.limelier.tables.references.USER_TIMEZONES
import mu.KotlinLogging
import org.jooq.impl.DSL
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

private val logger = KotlinLogging.logger {}

suspend fun main() {
    val kord = Kord(System.getenv("BOT_TOKEN"))
    val db = DSL.using(
        System.getenv("DB_URL"),
        System.getenv("DB_USER"),
        System.getenv("DB_PASSWORD")
    )

    val timestampCommand = kord.createGlobalChatInputCommand("timestamp", "Gives you a timestamp for a given time") {
        string("timezone", "Timezone; ex: Europe/Bucharest (preferred) or +3, UTC+3, GMT+3, EET (misleading depending on date)") {
            required = false
        }
        int("hour", "Hour (24h, default 0)") {
            minValue = 0
            maxValue = 23
        }
        int("minute", "Minute (default 0)") {
            minValue = 0
            maxValue = 59
        }
        int("second", "Second (default 0)") {
            minValue = 0
            maxValue = 59
        }
        int("year", "Year (default current year)") {
            minValue = 1970
        }
        int("month", "Month (default current month)") {
            minValue = 1
            maxValue = 12
        }
        int("day", "Day of month (default today's with no month, or '1' with a month)") {
            minValue = 1
            maxValue = 31
        }
        string("format", "Timestamp tag format") {
            required = false
            for (timestampFormat in TimestampFormat.values()) {
                choice(timestampFormat.description, timestampFormat.name)
            }
        }
    }

    val saveZoneCommand = kord.createGlobalChatInputCommand("savezone", "Saves a default timezone for future use") {
        string("timezone", "Timezone; ex: Europe/Bucharest (preferred) or +3, UTC+3, GMT+3, EET (misleading depending on date)") {
            required = true
        }
    }

    kord.on<ChatInputCommandInteractionCreateEvent> {
        val deferredResponse = interaction.deferEphemeralResponse()

        val command = interaction.command
        logger.info { "Sending /${command.rootName} response" }

        when (command.rootId) {
            timestampCommand.id -> {
                val hour = command.integers["hour"]?.toInt() ?: 0
                val minute = command.integers["minute"]?.toInt() ?: 0
                val second = command.integers["second"]?.toInt() ?: 0

                val timezone = command.strings["timezone"]?.let {
                    try {
                        ZoneId.of(it)
                    } catch (e: DateTimeException) {
                        deferredResponse.respond { content = "'$it' is not a valid timezone!" }
                        return@on
                    }
                }
                    ?: db.fetchOne(
                        USER_TIMEZONES,
                        USER_TIMEZONES.USER_ID.eq(interaction.user.id.value.toLong())
                    )
                        ?.let { ZoneId.of(it.timezone)}
                    ?: run {
                        deferredResponse.respond { content = "Timezone can only be left blank if one has been saved before with /savezone!" }
                        return@on
                    }

                val today = LocalDate.now(timezone)
                val year = command.integers["year"]?.toInt() ?: today.year
                val month = command.integers["month"]?.toInt() ?: today.month.value
                // most intuitive: use '1' as default if a month was specified, or today's day otherwise
                val day = command.integers["day"]?.toInt() ?: if (command.integers["month"] != null) 1 else today.dayOfMonth

                val format = TimestampFormat.valueOf(command.strings["format"] ?: TimestampFormat.DEFAULT.name)

                val timestamp = ZonedDateTime.of(year, month, day, hour, minute, second, 0, timezone)
                    .toInstant()
                    .epochSecond

                val tag = "<t:$timestamp${format.char?.let { ":$it" } ?: ""}>"
                deferredResponse.respond {
                    content = "Use `$tag`, it will look like $tag for you."
                }
            }
            saveZoneCommand.id -> {
                val userId = interaction.user.id
                val timezone = command.strings["timezone"]!!.let {
                    try {
                        ZoneId.of(it)
                    } catch (e: DateTimeException) {
                        deferredResponse.respond { content = "'$it' is not a valid timezone!" }
                        return@on
                    }
                }

                db.insertInto(USER_TIMEZONES)
                    .values(userId.value, timezone.id)
                    .onDuplicateKeyUpdate()
                    .set(USER_TIMEZONES.TIMEZONE, timezone.id)
                    .execute()

                deferredResponse.respond { content = "Timezone $timezone has been saved." }
            }
        }

    }

    logger.info { "Bot initialized, starting now" }
    kord.login()
}