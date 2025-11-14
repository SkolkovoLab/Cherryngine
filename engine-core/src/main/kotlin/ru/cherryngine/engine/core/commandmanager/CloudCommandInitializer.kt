package ru.cherryngine.engine.core.commandmanager

import io.leangen.geantyref.TypeToken
import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton

@Singleton
class CloudCommandInitializer(
    private val applicationContext: ApplicationContext,
    private val cloudCommandManager: CloudCommandManager,
    private val parsers: List<SArgumentParser<*>>,
) {
    @PostConstruct
    fun init() {
        // Регистрация парсеров
        parsers.forEach { parser ->
            cloudCommandManager.parserRegistry().registerParserSupplier(TypeToken.get(parser.type)) { parser }
        }

        // Регристрация комманд
        val beans = applicationContext.getBeansOfType(
            Any::class.java,
            Qualifiers.byStereotype(CloudCommand::class.java)
        )
        cloudCommandManager.annotationParser.parse(beans)
    }
}