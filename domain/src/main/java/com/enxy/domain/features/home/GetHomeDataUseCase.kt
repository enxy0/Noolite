package com.enxy.domain.features.home

import com.enxy.domain.UseCase
import com.enxy.domain.features.home.model.HomeData

interface GetHomeDataUseCase : UseCase<Unit, HomeData>
