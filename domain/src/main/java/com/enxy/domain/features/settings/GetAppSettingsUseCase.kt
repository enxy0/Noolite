package com.enxy.domain.features.settings

import com.enxy.domain.UseCase
import com.enxy.domain.features.settings.model.AppSettings

interface GetAppSettingsUseCase : UseCase<Unit, AppSettings>
