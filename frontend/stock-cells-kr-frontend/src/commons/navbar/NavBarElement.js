import React from 'react'
import { Navbar, Container, Nav, NavDropdown } from 'react-bootstrap'

const NavBarElement = () => {
  return (
	<Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="/">Intro</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            {/* <Nav.Link href="/buy-price-table">매수계획 tool</Nav.Link>
            <Nav.Link href="#link">Link</Nav.Link> */}
            <NavDropdown title="buying stock tool" id="basic-nav-dropdown">
              <NavDropdown.Item href="/buy-price-table">매수 계획표</NavDropdown.Item>
              <NavDropdown.Item href="/"> 매수 점도표 (매수 기록관리) [기획작업 중]</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="#action/3.4">
                Helpful link
              </NavDropdown.Item>
            </NavDropdown>
			<NavDropdown title="selling stock tool" id="basic-nav-dropdown">
				<NavDropdown.Item href="/">매도 계획표</NavDropdown.Item>
			</NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}

export default NavBarElement