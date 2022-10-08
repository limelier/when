import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent

suspend fun main() {
    val kord = Kord(System.getenv("BOT_TOKEN"))

    kord.createGlobalChatInputCommand("time", "Gives you a timestamp for a given time") {
        int("hour", "Hour (24h)") {
            required = true
            for (i in 0L..23L) {
                choice("$i".padStart(2, '0'), i)
            }
        }
        int("minute", "Minute") {
            required = false
        }
    }

    kord.on<ChatInputCommandInteractionCreateEvent> {
        val deferredResponse = interaction.deferEphemeralResponse()

        val command = interaction.command
        val hour = command.integers["hour"]!!
        val minute = command.integers["minute"]

        deferredResponse.respond {
            if (minute !in 0L..59L) {
                content = "invalid minute value of '$minute', must be between 0 and 59!"
                return@respond
            }

            content = "that's a total of ${hour * 60 + (minute ?: 0)} minutes!"
        }
    }

    kord.login()
}