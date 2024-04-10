using Microsoft.AspNetCore.Mvc;

namespace ImageServer.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PhotosController : ControllerBase
{
	private readonly IWebHostEnvironment _hostingEnvironment;

	public PhotosController(IWebHostEnvironment hostingEnvironment)
	{
		_hostingEnvironment = hostingEnvironment;
	}

	[HttpGet("{photoName}")]
	public IActionResult GetPhoto(string photoName)
	{
		var filePath = Path.Combine(_hostingEnvironment?.WebRootPath ?? "", "Photos", photoName);

		var fileExists = System.IO.File.Exists(filePath);
		if (!fileExists)
		{
			return NotFound();
		}

		return PhysicalFile(filePath, "image/webp"); 
	}
}

