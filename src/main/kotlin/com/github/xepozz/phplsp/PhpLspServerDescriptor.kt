package com.github.xepozz.phplsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor

class PhpLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "PHP Language") {
    val settings = PhpLspServiceSettings.getInstance(project)

    override fun isSupportedFile(file: VirtualFile) =
        settings.enabled && file.extension == "php"

    // the following supported only in IDEA Ultimate:
    //    override val lspDocumentColorSupport = super.lspDocumentColorSupport
    //    override val lspSemanticTokensSupport = super.lspSemanticTokensSupport
    //    override val lspGoToTypeDefinitionSupport = true

    override val lspCodeActionsSupport = super.lspCodeActionsSupport
    override val lspCommandsSupport = super.lspCommandsSupport
    override val lspDiagnosticsSupport = super.lspDiagnosticsSupport
    override val lspFindReferencesSupport = super.lspFindReferencesSupport
    override val lspFormattingSupport = super.lspFormattingSupport
    override val lspCompletionSupport = super.lspCompletionSupport
    override val lspGoToDefinitionSupport = true
    override val lspHoverSupport = true

    override val lspCommunicationChannel = LspCommunicationChannel.Socket(settings.port, settings.runCommand)
    override fun createCommandLine() = GeneralCommandLine(
        settings.binary,
        *settings.command
            .replace("%port%", settings.port.toString())
            .split(' ')
            .toTypedArray(),
    )
//    "serve",
//    "App\\Application",
//    "--port=%d"

//    override val lspCommunicationChannel = LspCommunicationChannel.Socket(settings.port, true)
//    override fun createCommandLine() = GeneralCommandLine(
//        "phpactor",
//        "language-server",
//        "--address=127.0.0.1:%d".format(settings.port),
//    )
}