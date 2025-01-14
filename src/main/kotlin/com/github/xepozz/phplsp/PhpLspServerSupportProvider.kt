package com.github.xepozz.phplsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem

internal class PhpLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerStarter) {
        if (file.extension == "php") {
            serverStarter.ensureServerStarted(PhpLspServerDescriptor(project))
        }
    }

    override fun createLspServerWidgetItem(
        lspServer: LspServer,
        currentFile: VirtualFile?
    ) = LspServerWidgetItem(
        lspServer,
        currentFile,
        AllIcons.Language.Php,
        PhpLspSettingsConfigurable::class.java,
    )
}

