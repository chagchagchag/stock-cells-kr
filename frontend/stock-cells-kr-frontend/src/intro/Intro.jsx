import React, { Fragment } from 'react'
import { IntroContainerStyle } from './styles/IntroContinerStyle.style'
import { IntroHeaderStyle, IntroMainHeaderStyle } from './styles/IntroHeaderStyle.style'
import './styles/style.css';

const Intro = () => {
  return (  
	<IntroContainerStyle>
		<IntroHeaderStyle>
			<IntroMainHeaderStyle>
				<h1 className='font-white'> Stock Cells KR </h1>
				<h5 className='font-white'> 매수/매도를 계획적으로 할수 있도록 돕기 위한 </h5>
				<h5 className='font-white'> 투자 자기관리 tool </h5>
			</IntroMainHeaderStyle>
		</IntroHeaderStyle>
	</IntroContainerStyle>
  )
}

export default Intro