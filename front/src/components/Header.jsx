import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Link} from "react-router-dom";

function Header() {
  return (
      <Navbar collapseOnSelect expand="lg" sticky="top" bg="dark" data-bs-theme="dark">
        <Container>
          <Navbar.Brand as={Link} to={"/"}> 1일 1백준 로그 </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to={"/"}>Home</Nav.Link>
              <Nav.Link as={Link} to={"/"}> 오늘의 커밋 </Nav.Link>
              <Nav.Link as={Link} to={"/calendar/week"}> 일주일의 커밋 </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
  )
}

export default Header
