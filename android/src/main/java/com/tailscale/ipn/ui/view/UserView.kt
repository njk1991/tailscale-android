// Copyright (c) Tailscale Inc & AUTHORS
// SPDX-License-Identifier: BSD-3-Clause

package com.tailscale.ipn.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.tailscale.ipn.R
import com.tailscale.ipn.ui.model.IpnLocal
import com.tailscale.ipn.ui.theme.minTextSize
import com.tailscale.ipn.ui.theme.short
import com.tailscale.ipn.ui.util.AutoResizingText

// Used to decorate UserViews.
// NONE indicates no decoration
// CURRENT indicates the user is the current user and will be "checked"
// SWITCHING indicates the user is being switched to and will be "loading"
// NAV will show a chevron
enum class UserActionState {
  CURRENT,
  SWITCHING,
  NAV,
  NONE
}

@Composable
fun UserView(
    profile: IpnLocal.LoginProfile?,
    onClick: () -> Unit = {},
    actionState: UserActionState = UserActionState.NONE
) {
  Box {
    profile?.let {
      ListItem(
          modifier = Modifier.clickable { onClick() },
          leadingContent = { Avatar(profile = profile, size = 36) },
          headlineContent = {
            AutoResizingText(
                text = profile.UserProfile.DisplayName,
                style = MaterialTheme.typography.titleMedium.short,
                minFontSize = MaterialTheme.typography.minTextSize,
                overflow = TextOverflow.Ellipsis)
          },
          supportingContent = {
            AutoResizingText(
                text = profile.NetworkProfile?.DomainName ?: "",
                style = MaterialTheme.typography.bodyMedium.short,
                minFontSize = MaterialTheme.typography.minTextSize,
                overflow = TextOverflow.Ellipsis)
          },
          trailingContent = {
            when (actionState) {
              UserActionState.CURRENT -> CheckedIndicator()
              UserActionState.SWITCHING -> SimpleActivityIndicator(size = 26)
              UserActionState.NAV -> Unit
              UserActionState.NONE -> Unit
            }
          })
    }
        ?: run {
          ListItem(
              modifier = Modifier.clickable { onClick() },
              headlineContent = {
                Text(
                    text = stringResource(id = R.string.accounts),
                    style = MaterialTheme.typography.titleMedium)
              })
        }
  }
}
