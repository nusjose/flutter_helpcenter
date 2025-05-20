import Flutter
import UIKit
import ZendeskCoreSDK
import SupportSDK
import CommonUISDK

public class ZendeskHelpCenterPluginClass: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_helpcenter", binaryMessenger: registrar.messenger())
    let instance = ZendeskHelpCenterPluginClass()
    registrar.addMethodCallDelegate(instance, channel: channel)
    registrar.addApplicationDelegate(instance)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {

       let method = call.method
              let dic = call.arguments as? Dictionary<String, Any>
              
       switch(method){
            case "initialize":
                self.initSupport(dictionary: dic!)

                break;

            case "updateIdentity":
                guard let name = dictionary["name"] as? String,
                              let email = dictionary["email"] as? String

                    else { return }

                let identity = Identity.createAnonymous(name: name, email: email)
                Zendesk.instance?.setIdentity(identity)
                break;

            case "showRequestList":
                  let config = RequestUiConfiguration()
                  let helpCenter = RequestUi.buildRequestList(with: [config])                  
                             let rootViewController:UIViewController! = UIApplication.shared.keyWindow?.rootViewController
                                   if (rootViewController is UINavigationController) {
                                       (rootViewController as! UINavigationController).pushViewController(helpCenter, animated:true)
                                   } else {
                                       let navigationController:UINavigationController! = UINavigationController(rootViewController:helpCenter)
                                     rootViewController.present(navigationController, animated:true, completion:nil)
                                   }
                break;

            case "showHelpCenterActivity":
                              let config = RequestUiConfiguration()
                              let helpCenter = HelpCenterUi.buildHelpCenterOverviewUi(withConfigs: [])
                                         let rootViewController:UIViewController! = UIApplication.shared.keyWindow?.rootViewController
                                               if (rootViewController is UINavigationController) {
                                                   (rootViewController as! UINavigationController).pushViewController(helpCenter, animated:true)
                                               } else {
                                                   let navigationController:UINavigationController! = UINavigationController(rootViewController:helpCenter)
                                                 rootViewController.present(navigationController, animated:true, completion:nil)
                                               }
                            break;
       default:
           print("Invalid method call!")
           break;
       
       }
  
  }

   func initSupport(dictionary: Dictionary<String, Any>) {
            guard let urlString = dictionary["urlString"] as? String,
                  let appId = dictionary["appId"] as? String,
                  let clientId = dictionary["clientId"] as? String,
                  let nameIdentifier = dictionary["nameIdentifier"] as? String,
                  let name = dictionary["name"] as? String,
                  let email = dictionary["email"] as? String

        else { return }

            Zendesk.initialize(appId: appId, clientId: clientId, zendeskUrl: urlString)

//             let identity: Identity
//             if nameIdentifier != "" {
//                 identity = Identity.createJwt(token: nameIdentifier)
//             } else {
//                 identity = Identity.createAnonymous(name: name, email: email)
//             }
//
//             Zendesk.instance?.setIdentity(identity)

               Zendesk.instance?.setIdentity(Identity.createAnonymous())

//             Zendesk.instance?.setIdentity(Identity.createAnonymous(name: name, email: email))
            Support.initialize(withZendesk: Zendesk.instance)

    
        }
}
