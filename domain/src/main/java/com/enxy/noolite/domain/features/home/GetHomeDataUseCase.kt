package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.home.model.HomeData

interface GetHomeDataUseCase : UseCase<Unit, HomeData>
