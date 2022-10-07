import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent
import dev.kord.rest.builder.message.AllowedMentionsBuilder

suspend fun main() {
    val kord = Kord(System.getenv("BOT_TOKEN"))

    kord.on<MessageCreateEvent> {
        if (message.author?.isBot != false) return@on
        if (!message.content.startsWith("!time ")) return@on

        val actualContent = message.content.substring("!time ".length)

        message.reply {
            allowedMentions = AllowedMentionsBuilder() // suppress all mentions
            content = "> " + actualContent.dummyTransform()
        }
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}