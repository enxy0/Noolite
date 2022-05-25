package com.enxy.domain.features.settings

import com.enxy.domain.UseCase
import com.enxy.domain.features.settings.model.AppSettings

interface UpdateAppSettingsUseCase : UseCase<AppSettings, Unit>
