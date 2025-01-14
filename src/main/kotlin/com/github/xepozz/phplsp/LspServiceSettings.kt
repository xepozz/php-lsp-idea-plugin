// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.xepozz.phplsp

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerManager

@Service(Service.Level.PROJECT)
@State(name = "PrismaServiceSettings", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class LspServiceSettings(val project: Project) :
    SimplePersistentStateComponent<LspServiceState>(LspServiceState()) {
    var serviceMode
        get() = state.serviceMode
        set(value) {
            val changed = state.serviceMode != value
            state.serviceMode = value
            if (changed) {
                restartPhpLspServerAsync(project)
            }
        }

    var port
        get() = state.port
        set(value) {
            val changed = state.port != value
            state.port = value
            if (changed) {
                restartPhpLspServerAsync(project)
            }
        }

    var command
        get() = state.command ?: ""
        set(value) {
            val changed = state.command != value
            state.command = value
            if (changed) {
                restartPhpLspServerAsync(project)
            }
        }

    var binary
        get() = state.binary ?: ""
        set(value) {
            val changed = state.binary != value
            state.binary = value
            if (changed) {
                restartPhpLspServerAsync(project)
            }
        }

    companion object {
        fun getInstance(project: Project) = project.service<LspServiceSettings>()
    }
}

class LspServiceState : BaseState() {
    var serviceMode by enum(LspServiceMode.ENABLED)
    var binary by string("/bin/lsp")
    var command by string("serve App\\\\Application --port=%d")
    var port by property(5007)
}

enum class LspServiceMode {
    ENABLED,
    DISABLED
}

fun restartPhpLspServerAsync(project: Project) {
    ApplicationManager.getApplication().invokeLater({
        val lspServerManager = LspServerManager.getInstance(project)

        lspServerManager.stopAndRestartIfNeeded(PhpLspServerSupportProvider::class.java)
    }, project.disposed)
}