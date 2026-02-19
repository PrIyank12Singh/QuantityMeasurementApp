# 📌 Quantity Measurement Application

## 🚀 Project Overview

This project demonstrates the **incremental evolution** of a Quantity Measurement system through three structured use cases:

- 🟢 **UC1** – Equality comparison for *Feet*
- 🟡 **UC2** – Equality comparison for *Feet and Inches*
- 🔵 **UC3** – Generic, scalable `QuantityLength` with cross-unit comparison

Each use case improves **design quality, scalability, and maintainability**.

---

# 🟢 UC1 – Feet Measurement Equality

## 🎯 Objective
Implement equality comparison for a single measurement unit: **Feet**.

## 🏗 Implementation
- Class: `Feet`
- Field: `double value`
- Overrides:
  - `equals(Object obj)`
  - `hashCode()`

### Equality Logic
Double.compare(this.value, other.value) == 0


## ✅ Features
✔ Reflexive equality  
✔ Symmetric equality  
✔ Transitive equality  
✔ Null safety  
✔ Type safety  
✔ Floating-point safe comparison  

## ⚠ Limitation
❌ Supports only Feet  
❌ Adding new units would cause duplication  

---

# 🟡 UC2 – Feet and Inches Equality

## 🎯 Objective
Extend UC1 to support **Inches** in addition to Feet.

## 🏗 Implementation
- Class: `Feet`
- Class: `Inches`

Both classes:
- Store a `double value`
- Override `equals()`
- Override `hashCode()`

🚫 Cross-unit comparison is NOT supported.

## ✅ Features
✔ Equality within same unit  
✔ Null safety  
✔ Type safety  
✔ Static comparison methods  
✔ Improved test coverage  

## ⚠ Design Issue

Violates **DRY (Don't Repeat Yourself) Principle**:

- Duplicate constructors  
- Duplicate `equals()` logic  
- Duplicate `hashCode()` logic  

Not scalable for future units.

---

# 🔵 UC3 – Generic QuantityLength (Refactored Design)

## 🎯 Objective
Refactor UC2 to:
- Remove duplication  
- Enable cross-unit equality  
- Improve scalability  

---

## 🏗 Implementation

### 1️⃣ Enum: `LengthUnit`

Defines conversion factors to base unit (Feet):

FEET(1.0)
INCH(1.0 / 12.0)


---

### 2️⃣ Class: `QuantityLength`

Encapsulates:
- `double value`
- `LengthUnit unit`

### 🔄 Equality Logic

Both values are converted to base unit before comparison:

Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0


---

## ✅ Features

✔ DRY Principle applied  
✔ Cross-unit comparison (1 ft = 12 in)  
✔ Enum-based type safety  
✔ Conversion abstraction  
✔ Scalable architecture  
✔ Full equality contract compliance  

---

## 🧪 Example Comparisons

| Comparison | Result |
|------------|--------|
| 1 ft vs 1 ft | ✅ true |
| 1 inch vs 1 inch | ✅ true |
| 1 ft vs 12 inch | ✅ true |
| 1 ft vs 2 ft | ❌ false |

---

# 🔄 Evolution Summary

| Feature | UC1 | UC2 | UC3 |
|----------|------|------|------|
| Feet support | ✅ | ✅ | ✅ |
| Inches support | ❌ | ✅ | ✅ |
| Cross-unit equality | ❌ | ❌ | ✅ |
| DRY compliant | ❌ | ❌ | ✅ |
| Scalable design | ❌ | ❌ | ✅ |

---

# 📚 Concepts Covered

- 📏 Object Equality Contract  
- 🛡 Encapsulation  
- 🔢 Floating-point comparison  
- ♻ DRY Principle  
- 🔄 Refactoring  
- 🧩 Enum usage  
- 🏗 Clean Architecture  
- 📈 Scalable Design  
- 🧠 Defensive Programming  

---

# 🏆 Final Outcome

This project demonstrates how simple equality logic evolves into a **clean, maintainable, and scalable design** using proper refactoring techniques.

It highlights the importance of:

- Eliminating duplication  
- Designing for scalability  
- Writing testable code  
- Following clean coding practices