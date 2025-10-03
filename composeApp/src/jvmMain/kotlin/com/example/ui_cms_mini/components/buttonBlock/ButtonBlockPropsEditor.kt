package com.example.ui_cms_mini.components.buttonBlock

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.common.model.ButtonComponent

@Composable
fun ButtonBlockPropsEditor(
    component: ButtonComponent,
    onUpdate: (ButtonComponent) -> Unit
) {
    var text by remember(component.id) { mutableStateOf(component.text) }
    var actionType by remember(component.id) { mutableStateOf(component.actionType) }
    var actionValue by remember(component.id) { mutableStateOf(component.actionValue) }

    Text("Text:")
    TextField(
        value = text,
        onValueChange = {
            text = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )

    Text("Action Type:")
    TextField(
        value = actionType,
        onValueChange = {
            actionType = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )

    Text("Action Value:")
    TextField(
        value = actionValue,
        onValueChange = {
            actionValue = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )
}