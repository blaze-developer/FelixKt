package com.blazedeveloper.felixkt.command

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.embed
import dev.kord.rest.request.RestRequestException
import io.github.classgraph.ClassGraph
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object CommandHandler {
    val commands =
        ClassGraph()
            .enableClassInfo()
            .scan()
            .getSubclasses(Command::class.java)
            .asSequence()
            .mapNotNull { it.loadClass(true)?.kotlin?.objectInstance as? Command }
            .associateBy { it.name }
    
    private val logger: Logger = LoggerFactory.getLogger("CommandHandler")

    suspend fun Kord.initializeCommands() {
        logger.info("Creating ${commands.size} commands...")

        createGlobalApplicationCommands {
            CommandHandler.commands.values.forEach {
                input(
                    name = it.name,
                    description = it.description,
                    builder = it.data
                )
            }


        }
        
        logger.info("Commands created!")

        on<GuildChatInputCommandInteractionCreateEvent> { handle(interaction) }
    }

    private suspend fun handle(interaction: GuildChatInputCommandInteraction) {
        val command = commands[interaction.invokedCommandName] ?:
            throw IllegalStateException("Non-Registered Command, ${interaction.invokedCommandName} Received!")

        val response =
            if (command.ephemeral) interaction.deferEphemeralResponse() else interaction.deferPublicResponse()

        try {
            command(ChatCommandInfo(
                response,
                interaction,
                interaction.command,
                interaction.kord
            ))
        } catch (e: Exception) {
            response.respond {
                embed {
                    title = "There was an error running that command! Oops :<"
                }
            }

            try {
                interaction.kord.getChannelOf<TextChannel>(Snowflake(1204677789421150208))?.createMessage {
                    embed {
                        title = "Error!"
                        description = "There was an error running /$command in \"${interaction.guild.asGuild().name}\"!"
                        field {
                            value = """
                                ```kt
                                ${e.stackTraceToString()}
                                ```
                            """.trimIndent()
                        }
                    }
                }
            } catch (e: RestRequestException) {
                interaction.kord.getChannelOf<TextChannel>(Snowflake(1204677789421150208))?.createMessage {
                    content = e.stackTraceToString()
                }
            }
        }
    }
}