package com.ramcosta.composedestinations.codegen.servicelocator

import com.ramcosta.composedestinations.codegen.commons.DestinationWithNavArgsMapper
import com.ramcosta.composedestinations.codegen.facades.CodeOutputStreamMaker
import com.ramcosta.composedestinations.codegen.facades.Logger
import com.ramcosta.composedestinations.codegen.model.Core
import com.ramcosta.composedestinations.codegen.writers.CoreExtensionsWriter
import com.ramcosta.composedestinations.codegen.writers.CustomNavTypesWriter
import com.ramcosta.composedestinations.codegen.writers.DestinationsWriter
import com.ramcosta.composedestinations.codegen.writers.NavGraphsObjectWriter
import com.ramcosta.composedestinations.codegen.writers.sub.NavArgResolver

internal interface ServiceLocatorAccessor {
    val logger: Logger
    val codeGenerator: CodeOutputStreamMaker
    val core: Core
    val generateNavGraphs: Boolean
}

internal val ServiceLocatorAccessor.customNavTypeWriter get() = CustomNavTypesWriter(
    codeGenerator,
    logger
)

internal val ServiceLocatorAccessor.destinationsWriter get() = DestinationsWriter(
    codeGenerator,
    logger,
    core,
    NavArgResolver()
)

internal val ServiceLocatorAccessor.navGraphsObjectWriter get() = NavGraphsObjectWriter(
    codeGenerator,
    logger,
    generateNavGraphs
)

internal val ServiceLocatorAccessor.coreExtensionsWriter get() = CoreExtensionsWriter(
    codeGenerator,
)

internal val ServiceLocatorAccessor.destinationWithNavArgsMapper get() = DestinationWithNavArgsMapper()