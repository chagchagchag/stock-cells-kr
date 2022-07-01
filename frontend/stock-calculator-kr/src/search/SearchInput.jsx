import React, { Component } from 'react';

class SearchInput extends Component {
	render() {
		return (
		<div class="row">
			<div class="col-md-6 m-auto">
			<h3 class="text-center mb-3">
				<i class="fa-solid fa-car-tunnel"></i> STOCK CELLS
			</h3>
			<input 
				type="text" 
				id="search" 
				class="form-control form-control-lg" 
				placeholder="종목 검색"/>
			</div>
        	<div id="match-list"></div>
		</div>
		);
	}
}

export default SearchInput;