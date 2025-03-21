package com.blazedeveloper.felixkt.command.util

import com.blazedeveloper.felixkt.command.ChatCommandInfo
import com.blazedeveloper.felixkt.command.Command
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis.arrowsCounterclockwise
import dev.kord.x.emoji.Emojis.heartbeat

object Ping : Command() {

    override val name = "ping"

    override val description = "Checks the bot's ping."

    override val ephemeral = false

    override val category = "Management"

    override suspend fun ChatCommandInfo.execute() {
        val sent = response.respond {
            embed {
                title = "Getting ping..."
                description = "Give us a moment!"
            }
        }

        sent.edit {
            embed {
                title = "Pong! :3"
                description = "This is how fast im running :3"
                field {
                    name = "$heartbeat Average Gateway Ping"
                    value = interaction.kord.gateway.averagePing?.toString() ?: "Unknown"
                }
                field {
                    name = "$arrowsCounterclockwise Round-trip Latency"
                    value = "${sent.message.id.timestamp - interaction.id.timestamp}"
                }
                image = "https://i.pinimg.com/originals/ac/b8/8f/acb88f71e5ed54072a24f647e28a9c3f.gif"
            }
        }
    }

}