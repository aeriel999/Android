using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace ShopApp.Entities.Identity;

public class User : IdentityUser<Guid>
{
    [StringLength(100)]
    public string? FirstName { get; set; }
    [StringLength(100)]
    public string? LastName { get; set; }
    [StringLength(100)]
    public string? Avatar { get; set; }
    public virtual ICollection<UserRole>? UserRoles { get; set; }
}
