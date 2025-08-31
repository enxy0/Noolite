package com.enxy.noolite.domain.common

import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.UseCase

interface GetAppSettingsUseCase : UseCase<Unit, AppSettings>
