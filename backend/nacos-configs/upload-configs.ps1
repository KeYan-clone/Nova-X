# ========================================
# Nacos 配置自动上传脚本
# ========================================

param(
    [string]$NacosAddr = "localhost:8848",
    [string]$Namespace = "dev",
    [string]$Username = "nacos",
    [string]$Password = "nacos"
)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Nacos 配置上传工具" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Nacos API 地址
$nacosUrl = "http://$NacosAddr/nacos/v1/cs/configs"
$configDir = "$PSScriptRoot"

Write-Host "配置目录: $configDir" -ForegroundColor Yellow
Write-Host "Nacos 地址: $NacosAddr" -ForegroundColor Yellow
Write-Host "命名空间: $Namespace" -ForegroundColor Yellow
Write-Host ""

# 获取所有 yaml 配置文件
$configFiles = Get-ChildItem -Path $configDir -Filter "*.yaml" | Where-Object { $_.Name -ne "README.md" }

if ($configFiles.Count -eq 0) {
    Write-Host "❌ 未找到配置文件" -ForegroundColor Red
    exit 1
}

Write-Host "找到 $($configFiles.Count) 个配置文件:" -ForegroundColor Green
$configFiles | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor Gray }
Write-Host ""

# 上传配置文件
$successCount = 0
$failCount = 0

foreach ($file in $configFiles) {
    $dataId = $file.Name
    $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8

    Write-Host "正在上传: $dataId ... " -NoNewline

    try {
        $body = @{
            dataId = $dataId
            group = "DEFAULT_GROUP"
            tenant = $Namespace
            content = $content
            type = "yaml"
        }

        $response = Invoke-RestMethod -Uri $nacosUrl -Method POST -Body $body -ContentType "application/x-www-form-urlencoded" -ErrorAction Stop

        if ($response -eq "true") {
            Write-Host "✅ 成功" -ForegroundColor Green
            $successCount++
        } else {
            Write-Host "❌ 失败" -ForegroundColor Red
            $failCount++
        }
    } catch {
        Write-Host "❌ 失败: $($_.Exception.Message)" -ForegroundColor Red
        $failCount++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "上传完成！" -ForegroundColor Cyan
Write-Host "成功: $successCount 个" -ForegroundColor Green
Write-Host "失败: $failCount 个" -ForegroundColor $(if ($failCount -gt 0) { "Red" } else { "Gray" })
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "💡 提示：" -ForegroundColor Yellow
Write-Host "  1. 访问 Nacos 控制台: http://$NacosAddr/nacos" -ForegroundColor Gray
Write-Host "  2. 在 配置管理 > 配置列表 中查看已上传的配置" -ForegroundColor Gray
Write-Host "  3. 根据需要修改配置中的 MySQL、Redis、Kafka 地址" -ForegroundColor Gray
Write-Host ""
