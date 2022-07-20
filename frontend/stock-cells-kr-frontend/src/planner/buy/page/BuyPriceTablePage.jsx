import React from 'react'
import BuyPriceTable from '../BuyPriceTable'
import SearchCompanyInput from '../../../search/components/SearchCompanyInput'
import TickerList from '../../../search/components/TickerList'

const BuyPriceTablePage = () => {
  return (
	<div className='container mt-5'>
		<SearchCompanyInput></SearchCompanyInput>
		<TickerList></TickerList>
		<BuyPriceTable></BuyPriceTable>
	</div>
  )
}

export default BuyPriceTablePage