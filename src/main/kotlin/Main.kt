import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

suspend fun main() {
    val kord = Kord(System.getenv("BOT_TOKEN"))

    kord.createGlobalChatInputCommand("timestamp", "Gives you a timestamp for a given time") {
        int("hour", "Hour (24h)") {
            required = true
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

    kord.on<ChatInputCommandInteractionCreateEvent> {
        val deferredResponse = interaction.deferEphemeralResponse()

        val command = interaction.command

        val hour = command.integers["hour"]!!.toInt()
        val minute = command.integers["minute"]?.toInt()  ?: 0
        val second = command.integers["second"]?.toInt()  ?: 0

        val timezone = ZoneId.systemDefault() // todo let people change
        val today = LocalDate.now(timezone)
        val year = command.integers["year"]?.toInt() ?: today.year
        val month = command.integers["month"]?.toInt() ?: today.month.value
        // most intuitive: use '1' as default if a month was specified, or today's day otherwise
        val day = command.integers["day"]?.toInt()  ?: if (command.integers["month"] != null) 1 else today.dayOfMonth

        val format = TimestampFormat.valueOf(command.strings["format"] ?: TimestampFormat.DEFAULT.name)

        val timestamp = ZonedDateTime.of(year, month, day, hour, minute, second, 0, timezone)
            .toInstant()
            .epochSecond

        val tag = "<t:$timestamp${format.char?.let { ":$it" } ?: ""}>"
        deferredResponse.respond {
            content = "Use `$tag`, it will look like $tag for you."
        }
    }

    kord.login()
}