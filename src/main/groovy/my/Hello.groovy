package my

class ConfigurationContainer {
    private Map<String, Closure> configurations = [:]

    def init(String configurationName, Closure handler) {
        configurations[configurationName] = handler
    }

    boolean hasConfig(String name) {
        configurations.containsKey(name)
    }

    Closure getConfig(String name) {
        return configurations.get(name)
    }
}

class DependencyHandler {

    private ConfigurationContainer configurationContainer

    DependencyHandler(configurationContainer) {
        this.configurationContainer = configurationContainer
    }

    @Override
    Object invokeMethod(String name, Object args) {
        if (configurationContainer.hasConfig(name)) {
            return configurationContainer.getConfig(name).call(args)
        } else {
            return println("Can't find config '$name', please check your config")
        }
    }
}


def createDependencyHandler() {
    ConfigurationContainer configurationContainer = new ConfigurationContainer()
    configurationContainer.init("compile", { lib -> println("### loading $lib and use it when compiling main code") })
    configurationContainer.init("testCompile", { lib -> println("### loading $lib and use it when compiling tests") })
    return new DependencyHandler(configurationContainer)
}

def dependencies(Closure configClosure) {
    dependencyHandler = createDependencyHandler()
    configClosure.resolveStrategy = Closure.DELEGATE_FIRST
    configClosure.delegate = dependencyHandler
    configClosure()
}

dependencies {
    compile 'org.apache.commons:commons-lang3:3.4'
    others 'anything'
}

