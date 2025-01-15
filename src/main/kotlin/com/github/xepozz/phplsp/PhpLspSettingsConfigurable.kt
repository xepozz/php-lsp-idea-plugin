// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.xepozz.phplsp

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.UiDslUnnamedConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText

class PhpLspSettingsConfigurable(project: Project) : UiDslUnnamedConfigurable.Simple(), Configurable {
    private val settings = PhpLspServiceSettings.getInstance(project)

    override fun Panel.createContent() {
        group("Language Server Configuration") {
            row {
                checkBox("Enabled").bindSelected(settings::enabled)
            }

            row {
                checkBox("Run language server automatically").bindSelected(settings::runCommand)
            }

            separator()

            row {
                textFieldWithBrowseButton(project = settings.project)
                    .label("Binary:")
                    .align(Align.FILL)
                    .bindText(settings::binary)
            }

            row {
                textField()
                    .label("Command:")
                    .align(Align.FILL)
                    .bindText(settings::command)

                comment("Use %port% to substitute the port number. All special symbols will be escaped automatically.")
            }

            row {
                intTextField(range = IntRange(1, 65535), keyboardStep = 1)
                    .label("Port:")
                    .bindIntText(settings::port)
            }

        }
    }

    override fun getDisplayName() = MyBundle.message("settings.configurable.title")
}