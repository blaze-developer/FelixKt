package com.blazedeveloper.felixkt

import com.blazedeveloper.felixkt.command.CommandHandler.initializeCommands
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import org.slf4j.LoggerFactory

suspend fun main(args: Array<String>) {
    val kord = Kord(args.firstOrNull() ?: error("No token provided"))
    kord.initializeCommands()

    kord.on<ReadyEvent> {
        LoggerFactory.getLogger("EventHandler").info("Logged in as ${self.username}")
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}