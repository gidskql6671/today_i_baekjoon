import {Button, Image} from "react-bootstrap";
import githubMark from "../assets/github-mark.svg";

const GithubLoginButton = () => {
  const clientId = 'fd567229463b6f2452fb';
  const redirectUrl = 'http://localhost:5173/login/callback'
  const githubURL = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUrl}`
  const handleLogin = ()=>{
    window.location.href = githubURL
  }

  return (
      <Button variant="outline-secondary" onClick={handleLogin}>
        <Image src={githubMark} />
        <p className={"mt-2 text-center"}>깃허브 로그인</p>
      </Button>
  )
}

export default GithubLoginButton