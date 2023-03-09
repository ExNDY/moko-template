/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit
import MultiPlatformLibrary
import MultiPlatformLibrarySwift
import SkyFloatingLabelTextField

class ConfigViewController: UIViewController {
    @IBOutlet private var tokenField: SkyFloatingLabelTextField!
    @IBOutlet private var languageField: SkyFloatingLabelTextField!
    
    private var viewModel: ConfigViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = AppComponent.factory.configFactory.createConfigViewModel()
        
        viewModel.actions.subscribe(onCollect: { [weak self] action in
            self?.handeAction(action: ConfigViewModelActionKs(action!))
            
        })
        
        
        // binding methods from https://github.com/icerockdev/moko-mvvm
        tokenField.bindTextTwoWay(liveData: viewModel.apiTokenField.data)
        tokenField.bindText(liveData: viewModel.apiTokenField.error)
        
        languageField.bindTextTwoWay(liveData: viewModel.languageField.data)
        languageField.bindText(liveData: viewModel.languageField.error)
    }
    
    @IBAction func onSubmitPressed() {
        viewModel.onSubmitPressed()
    }
    
    deinit {
        // clean viewmodel to stop all coroutines immediately
        viewModel.onCleared()
    }
    
    private func handeAction(action: ConfigViewModelActionKs) {
        switch action {
        case .routeToNews: performSegue(withIdentifier: "routeToNews", sender: nil)
        }
    }
}
