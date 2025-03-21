package com.blazedeveloper.felixkt.command

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.DeferredMessageInteractionResponseBehavior
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

typealias Data = ChatInputCreateBuilder.() -> Unit

data class ChatCommandInfo(
    val response: DeferredMessageInteractionResponseBehavior,
    val interaction: GuildChatInputCommandInteraction,
    val command: InteractionCommand,
    val kord: Kord,
)

// To generalize this to multiple types of command, type argument the CommandInfo arguments,
// and maybe the interaction type and a way to register them?

abstract class Command {

    /** The name of this command **/
    abstract val name: String

    /** The description of this command **/
    abstract val description: String

    /** The category of this command **/
    open val category: String = "Misc"

    /** The data of this command (optional) **/
    open val data: Data = {}

    /** Whether this command will be ephemeral (hidden from other users) (optional) **/
    open val ephemeral: Boolean = false

    /** Carries out the command given to the bot using the interaction and response objects **/
    abstract suspend fun ChatCommandInfo.execute()

    /** Calls this commands execute method as a receiver, this is what the CommandHandler should call. **/
    suspend operator fun invoke(info: ChatCommandInfo) = info.execute()

    override fun toString() = name

    val logger: Logger by lazy {
        LoggerFactory.getLogger(name.replaceFirstChar { it.uppercaseChar() })
    }

}