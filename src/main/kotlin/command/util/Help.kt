package com.blazedeveloper.felixkt.command.util

import com.blazedeveloper.felixkt.command.ChatCommandInfo
import com.blazedeveloper.felixkt.command.Command
import com.blazedeveloper.felixkt.command.CommandHandler
import com.blazedeveloper.felixkt.command.Data
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.EmbedBuilder

object Help : Command() {
    /** The name of this command **/
    override val name = "help"

    /** The description of this command **/
    override val description = "Get all the commands that can be run!"

    override val data: Data = {
        string("category", "The category of commands to look at") {
            categories.keys.forEach { choice(it, it) }
            required = true
        }
    }

    private val categories by lazy { CommandHandler.commands.values.groupBy { it.category } }

    private val categoryEmbeds by lazy {
        categories.keys
            .associateWith { cat -> EmbedBuilder().apply {
                title = "$cat Commands"
                categories[cat]!!.forEach {
                    field {
                        name = it.name
                        value = it.description
                        inline = true
                    }
                }
            }
        }
    }

    /** Carries out the command given to the bot using the interaction and response objects **/
    override suspend fun ChatCommandInfo.execute() {
        val category = requireNotNull(command.strings["category"])

        response.respond {
            embeds = mutableListOf(categoryEmbeds[category]!!)
        }
    }

}