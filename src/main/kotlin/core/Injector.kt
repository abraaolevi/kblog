package core

import core.database.Connection
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import repository.UserRepository
import service.AuthService
import service.UserService
import utility.Jwt
import utility.Security

object Injector {
    val get = Kodein {
        // Database
        bind<Connection>() with singleton { Connection() }

        // Utils
        bind<Security>() with singleton { Security() }
        bind<Jwt>() with singleton { Jwt() }

        // Repositories
        bind<UserRepository>() with provider { UserRepository(security = instance()) }

        // Services
        bind<AuthService>() with provider { AuthService(security = instance(), jwt = instance(), userRepo = instance()) }
        bind<UserService>() with provider { UserService(userRepo = instance()) }

    }
}