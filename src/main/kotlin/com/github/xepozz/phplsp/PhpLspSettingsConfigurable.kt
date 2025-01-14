// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.xepozz.phplsp

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.UiDslUnnamedConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindText

class PhpLspSettingsConfigurable(project: Project) : UiDslUnnamedConfigurable.Simple(), Configurable {
    private val settings = LspServiceSettings.getInstance(project)

    override fun Panel.createContent() {
        group("Language Server Configuration") {
            buttonsGroup {
                row {
                    radioButton("Enabled", LspServiceMode.ENABLED)
                }
                row {
                    radioButton("Disabled", LspServiceMode.DISABLED)
                }
            }.apply {
                bind(settings::serviceMode)
            }

            separator()

            row {
                textFieldWithBrowseButton()
                    .label("Binary")
                    .bindText(settings::binary)
            }
//            row {
//                textField()
//                    .label("Command")
//                    .bindText(settings::command)
//            }
            row {
                textField()
                    .label("Port")
                    .bindIntText(settings::port)
            }

        }
    }

    override fun getDisplayName() = MyBundle.message("settings.configurable.title")
}