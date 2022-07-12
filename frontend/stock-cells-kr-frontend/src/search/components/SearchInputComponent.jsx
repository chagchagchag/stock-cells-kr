import React, { Component } from 'react';
import TickerSearchItem from '../../commons/TickerSearchItem'

class SearchInputComponent extends Component {
	state = {
		text: '',
		list: [],
		selected: ''
	};
	
	componentDidMount(){
		// ..
	}

	renderTickerSearchItem = (list) => {
		if(list.length == 0) return [];

		var searchResultEl = [];

		for(var i=0; i<list.length; i++){
			var tickerItem = list[i];

			searchResultEl.push(
				<TickerSearchItem
					key={tickerItem.ticker}
					ticker={tickerItem.ticker}
					companyName={tickerItem.companyName}
					// onClick={}
				/>
			)
		}
		return searchResultEl;
	}

	renderSearchResult = (list) => {
		if(list.length === 0) return [];
		
		var searchResultEl = [];

		for(var i=0; i<list.length; i++){
			var each = list[i];
			searchResultEl.push(
				<div key={each.ticker} value={each.ticker} className="card card-body mb-1" 
					 onClick={
						 function(e){
							//  this.state.companyName = 
							// console.log("PROPS >>> " + this.props);
						}.bind(this)}
				>
					<small key={each.ticker} ticker={each.ticker} onClick={function(e){console.log(this.props)}.bind(this)}>회사명 : {each.companyName} / 종목코드 : {each.ticker}</small>
				</div>
			)
		}
		return searchResultEl;
	};

	handleInputChange = (e) => {
		if(e.target.value === ''){
			this.setState({
				text: '종목명 입력',
				list: []
			});
			return;
		}

		this.setState({
			text: e.target.value
		});

		// fetch('data/dump-stock-list.json')
		fetch('/ticker/stock?companyName='+this.state.text)
			.then(function(result){
				return result.json(); 
			})
			.then(function(json){
				console.log(json);
				this.setState({list:json});
			}.bind(this));

	};

	render() {
		return (
		<div className="row">
			<div className="col-md-6 m-auto">
			{/* <h3 className="text-center mb-3">
				<i className="fa-solid fa-car-tunnel"></i> STOCK CELLS
			</h3> */}
			<input 
				type="text" 
				id="search" 
				className="form-control form-control-lg" 
				onChange={function(e){
					this.handleInputChange(e);
					this.props.onChange(this.state.companySearchResult);
				}.bind(this)}
				placeholder="종목 검색"/>
			</div>
			{/* <div>
				{this.renderTickerSearchItem(this.state.list)}
			</div> */}
		</div>
		);
	}
}

export default SearchInputComponent;