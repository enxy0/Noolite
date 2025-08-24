package com.enxy.noolite.domain.features.settings

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings

interface GetAppSettingsUseCase : UseCase<Unit, AppSettings>
