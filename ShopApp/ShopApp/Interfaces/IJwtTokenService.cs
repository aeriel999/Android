using ShopApp.Entities.Identity;

namespace ShopApp.Interfaces;

public interface IJwtTokenService
{
	Task<string> CreateToken(User user);
}
