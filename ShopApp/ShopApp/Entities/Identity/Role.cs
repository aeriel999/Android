using Microsoft.AspNetCore.Identity;

namespace ShopApp.Entities.Identity;

public class Role : IdentityRole<Guid>
{
    public virtual ICollection<UserRole>? UserRoles { get; set; }
}
