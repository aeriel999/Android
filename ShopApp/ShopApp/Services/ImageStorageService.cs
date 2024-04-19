using ErrorOr;
using Microsoft.AspNetCore.Identity;
using ShopApp.Entities;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Formats.Webp;
using SixLabors.ImageSharp.Processing;

namespace ShopApp.Services;

public class ImageStorageService
{
	public async Task<ErrorOr<Category>> AddAvatarAsync(Category category, IFormFile file)
	{
		try
		{
			string imageName = Path.GetRandomFileName() + ".webp";

			var uploadFolderPath = Path.Combine(Directory.GetCurrentDirectory(), "images", "category");

			if (!Directory.Exists(uploadFolderPath))
			{
				Directory.CreateDirectory(uploadFolderPath);
			}

			if (category.Image != null)
			{
				var delFilePath = Path.Combine(uploadFolderPath, category.Image!);

				if (File.Exists(delFilePath))
					File.Delete(delFilePath);
			}

			string dirSaveImage = Path.Combine(uploadFolderPath, imageName);

			using (MemoryStream ms = new())
			{
				await file.CopyToAsync(ms);

				using (var image = Image.Load(ms.ToArray()))
				{
					image.Mutate(x =>
					{
						x.Resize(new ResizeOptions
						{
							Size = new Size(1200, 1200),
							Mode = ResizeMode.Max
						});
					});

					using var stream = File.Create(dirSaveImage);
					await image.SaveAsync(stream, new WebpEncoder());
				}
			}

			category.Image = imageName;

			return category;
		}
		catch (Exception ex)
		{
			return Error.Unexpected(ex.Message.ToString());
		}
	}
}
