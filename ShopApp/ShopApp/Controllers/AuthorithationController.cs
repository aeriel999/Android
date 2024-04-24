using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using ShopApp.DTOs;
using ShopApp.Entities.Identity;
using ShopApp.Interfaces;

namespace ShopApp.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AuthorithationController : Controller
{
	private readonly UserManager<User> _userManager;
	private readonly IJwtTokenService _jwtTokenService;

	public AuthorithationController(UserManager<User> userManager, IJwtTokenService jwtTokenService)
	{
		_userManager = userManager;
		_jwtTokenService = jwtTokenService;
	}


	[HttpPost("login")]
	public async Task<IActionResult> Login([FromBody] LoginDto model)
	{
		var user = await _userManager.FindByEmailAsync(email: model.Email);

		if (user == null) return BadRequest();

		var isAuth = await _userManager.CheckPasswordAsync(user, model.Password);

		if (!isAuth) return BadRequest();

		var token = await _jwtTokenService.CreateToken(user);

		if (token == null) return BadRequest();

		return Ok(new { token });
	}
}
