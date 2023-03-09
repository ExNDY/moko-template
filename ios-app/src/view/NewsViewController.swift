/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryUnits

class NewsViewController: UIViewController {
    @IBOutlet private var tableView: UITableView!
    @IBOutlet private var activityIndicator: UIActivityIndicatorView!
    @IBOutlet private var emptyView: UIView!
    @IBOutlet private var errorView: UIView!
    @IBOutlet private var errorLabel: UILabel!
    
    private var viewModel: ListViewModel<News>!
    private var dataSource: TableUnitsSource!
    private var refreshControl: UIRefreshControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = AppComponent.factory.newsFactory.createListViewModel()
        viewModel.onCreated()

        // binding methods from https://github.com/icerockdev/moko-mvvm
        activityIndicator.bindHidden(liveData: viewModel.state.isLoadingState())
        tableView.bindHidden(liveData: viewModel.state.isSuccessState())
        emptyView.bindHidden(liveData: viewModel.state.isEmptyState())
        errorView.bindHidden(liveData: viewModel.state.isErrorState())

        // in/out generics of Kotlin removed in swift, so we should map to valid class
        let errorText: LiveData<StringDesc> = viewModel.state.error()
            .map { $0 as? StringDesc ?? RawStringDesc(string: "") } as! LiveData<StringDesc>
        errorLabel.bindText(liveData: errorText.localized_())

        // datasource from https://github.com/icerockdev/moko-units
        dataSource = TableUnitsSourceKt.default(for: tableView)

        // manual bind to livedata, see https://github.com/icerockdev/moko-mvvm
        viewModel.state.data().addObserver { [weak self] news in
            guard let itemsNews = news as? [News_] else { return }
            let items = itemsNews.map{ newsItem in
                toTableUnit(news: newsItem)
            }
            
            self?.dataSource.unitItems = items
            self?.tableView.reloadData()
        }
        
        refreshControl = UIRefreshControl()
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(onRefresh), for: .valueChanged)
    }
    
    @IBAction func onRetryPressed() {
        viewModel.onRetryClick()
    }
    
    @objc func onRefresh() {
        viewModel.onRefresh { [weak self] in
            self?.refreshControl.endRefreshing()
        }
    }
}

private func toTableUnit(news: News_) -> TableUnitItem {
    return UITableViewCellUnit<NewsTableViewCell>(
        data: NewsTableViewCell.CellModel(
            id: news.id,
            title: news.title.toNormalize(),
            description: news.description
        ),
        itemId: news.id,
        configurator: nil
    )
}

extension String? {
    func toNormalize() -> String {
        if self == nil {
            return ""
        } else {
            return self!
        }
    }
}
