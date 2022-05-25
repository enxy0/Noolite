package com.enxy.noolite.presentation.utils.intent

import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentActionsProviderImpl(
    private val context: Context
) : IntentActionsProvider {

    companion object {
        private const val githubLink = "https://www.github.com/enxy0/Noolite"
    }

    override fun openGithubProject() {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(githubLink))
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.resolveActivity(context.packageManager)?.let {
            context.startActivity(intent)
        }
    }
}
