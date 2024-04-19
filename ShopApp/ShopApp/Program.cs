using Microsoft.Extensions.FileProviders;
using ShopApp;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();

builder.Services.AddSwagger();

builder.Services.AddPersistence();

builder.Services.AddMappings();

var app = builder.Build();

// Configure the HTTP request pipeline.
//if (app.Environment.IsDevelopment())
//{
	app.UseSwagger();
	app.UseSwaggerUI();
//}

var dir = Path.Combine(Directory.GetCurrentDirectory(), "images");
if (!Directory.Exists(dir))
{
	Directory.CreateDirectory(dir);
}

app.UseStaticFiles(new StaticFileOptions
{
	FileProvider = new PhysicalFileProvider(dir),
	RequestPath = "/images"
});

app.UseCors(options =>
	options.SetIsOriginAllowed(origin => true)
		.AllowAnyHeader()
		.AllowCredentials()
		.AllowAnyMethod());

//app.UseHttpsRedirection();

app.MapControllers();

app.Run();
