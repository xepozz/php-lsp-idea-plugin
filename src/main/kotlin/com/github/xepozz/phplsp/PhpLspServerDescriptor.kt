package com.github.xepozz.phplsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspCommunicationChannel
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.util.io.BaseDataReader
import com.intellij.util.io.BaseOutputReader
import com.intellij.util.io.BaseOutputReader.Options.forMostlySilentProcess

class PhpLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "PHP Language") {
    val settings = LspServiceSettings.getInstance(project)

    override fun isSupportedFile(file: VirtualFile) =
        settings.serviceMode == LspServiceMode.ENABLED && file.extension == "php"

    override val lspDocumentColorSupport = super.lspDocumentColorSupport
    override val lspCodeActionsSupport = super.lspCodeActionsSupport
    override val lspCommandsSupport = super.lspCommandsSupport
    override val lspDiagnosticsSupport = super.lspDiagnosticsSupport
    override val lspFindReferencesSupport = super.lspFindReferencesSupport
    override val lspFormattingSupport = super.lspFormattingSupport

    override val lspSemanticTokensSupport = super.lspSemanticTokensSupport
    override val lspGoToDefinitionSupport = true
    override val lspGoToTypeDefinitionSupport = true
    override val lspCompletionSupport = super.lspCompletionSupport
    override val lspHoverSupport = true

    override val lspCommunicationChannel = LspCommunicationChannel.Socket(settings.port, false)

    override fun startServerProcess(): OSProcessHandler {
//        Thread.sleep(1)
        val startingCommandLine = createCommandLine().withCharset(Charsets.UTF_8)
//        LOG.info("$this: starting LSP server: $startingCommandLine")
        return object : OSProcessHandler(startingCommandLine) {
            override fun readerOptions(): BaseOutputReader.Options = object : BaseOutputReader.Options() {
                override fun policy(): BaseDataReader.SleepingPolicy = BaseDataReader.SleepingPolicy.BLOCKING

                // Must not loose '\r' in "\r\n" line endings. They affect char count, which must match `Content-Length`
                override fun splitToLines(): Boolean = true
            }
        }
    }
    override fun createCommandLine() = GeneralCommandLine(
        settings.binary,
        "serve",
        "App\\Application",
        "--port=%d".format(settings.port),
    )
}