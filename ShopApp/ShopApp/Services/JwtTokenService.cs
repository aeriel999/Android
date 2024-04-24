using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using ShopApp.Entities.Identity;
using ShopApp.Interfaces;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace ShopApp.Services;

public class JwtTokenService : IJwtTokenService
{
	private readonly IConfiguration _config;
	private readonly UserManager<User> _userManager;

	public JwtTokenService(IConfiguration config, UserManager<User> userManager)
	{
		_config = config;
		_userManager = userManager;
	}

	public async Task<string> CreateToken(User user)
	{
		var roles = await _userManager.GetRolesAsync(user);
		List<Claim> claims = new()
			{
				new Claim("email", user.Email!),
				new Claim("name", $"{user.FirstName} {user.LastName}"),
				new Claim("avatar", user.Avatar ?? string.Empty)
			};

		foreach (var role in roles)
			claims.Add(new Claim("roles", role));

		var signinKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(
			_config.GetValue<String>("JwtSecretKey")));
		var signinCredentials = new SigningCredentials(signinKey, SecurityAlgorithms.HmacSha256);

		var jwt = new JwtSecurityToken(
			signingCredentials: signinCredentials,
			expires: DateTime.Now.AddDays(10),
			claims: claims
		);
		return new JwtSecurityTokenHandler().WriteToken(jwt);
	}
}
