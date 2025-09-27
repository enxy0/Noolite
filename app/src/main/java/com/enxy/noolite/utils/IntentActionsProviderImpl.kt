package com.enxy.noolite.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.enxy.noolite.core.ui.IntentActionsProvider

class IntentActionsProviderImpl(
    private val context: Context
) : IntentActionsProvider {

    companion object {
        private const val GITHUB_PROJECT_URL = "https://www.github.com/enxy0/Noolite"
    }

    override fun openGithubProject() {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(GITHUB_PROJECT_URL.toUri())
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.resolveActivity(context.packageManager)?.let {
            context.startActivity(intent)
        }
    }
}
