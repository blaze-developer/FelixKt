package com.blazedeveloper.felixkt.command.util

import com.blazedeveloper.felixkt.command.ChatCommandInfo
import com.blazedeveloper.felixkt.command.Command
import com.blazedeveloper.felixkt.command.Data
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.interaction.user

object Hug : Command() {

    override val name: String = "hug"

    override val description: String = "Hugs another user"

    override val category = "Emotes"

    override val data: Data = {
        user("user", "The user to hug") {
            required = true
        }
    }

    override suspend fun ChatCommandInfo.execute() {
        val user = requireNotNull(command.users["user"])
        response.respond { content = "Hey, ${user.mention}! ${interaction.user.mention} hugged you :3" }
    }

}