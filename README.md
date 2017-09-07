## Introduction:
Eclipser will automatically convert Eclipse launch configurations into IntelliJ IDEA configurations:

  * local Java application Eclipse launcher into run configuration
  * remote Java application Eclipse launcher into remote run configuration
  * program launch Eclipse launcher into external tool in Tools menu
  * Maven launch configuration into Maven run configuration
  * Ant launch configuration into Ant target run configuration

## How to use:
  * Eclipser is available in Project and Commander views.
  * Context menu for supported launch files will contain "Convert with Eclipser" item.

## Functionality limitations:
  * Current support for two Eclipse macros only:
    - $workspace_loc
    - $env_var
  * Eclipse UI launch configuration is not supported:
    - org.eclipse.pde.ui.RuntimeWorkbench

## Known issues:
  * None

## Disclaimer:
  * Plugin is currently in beta. Please report any errors and suggestions to the link below.

## Support:
  * Email: mclovin@kukido.com
  * Support tickets: [https://github.com/kukido/eclipser/issues](https://github.com/kukido/eclipser/issues)

## For developers:
  * The project has dependency on Maven, Properties and Ant plugins. They all included with the project.
  * If you are developing in IntelliJ Ultimate, you have to add Netty jar from lib/netty as project library.
  * You will have to redefine `IntelliJ Platform Plugin SDK` location once you open the project.
